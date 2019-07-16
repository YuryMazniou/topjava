package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            saveRole(new ArrayList<>(user.getRoles()), newKey.intValue());
            user=get(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User>users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id = r.user_id WHERE u.id=?",
                getExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON u.id=r.user_id WHERE email=?",
                getExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users u LEFT JOIN user_roles r ON u.id=r.user_id",getExtractor());
    }

    private ResultSetExtractor<List<User>> getExtractor(){
        return new ResultSetExtractor<List<User>>(){
            @Override
            public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Integer,User> map=new HashMap<>();
                while (rs.next()){
                    int id=rs.getInt("id");
                    String name=rs.getString("name");
                    System.out.println(name);
                    String email=rs.getString("email");
                    String password=rs.getString("password");
                    int calor=rs.getInt("calories_per_day");
                    boolean enabl=rs.getBoolean("enabled");
                    Date registr=rs.getDate("registered");
                    Role roles=Role.valueOf(rs.getString("role"));
                    if(map.containsKey(id))map.get(id).getRoles().add(roles);
                    else map.put(id,new User(id,name,email,password,calor,enabl,registr,
                            new ArrayList<>(Collections.singleton(roles))));
                }

                return map.values().stream().sorted(new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        int name=o1.getName().compareTo(o2.getName());
                        return name==0?o1.getEmail().compareTo(o2.getEmail()):name;
                    }
                }).collect(Collectors.toList());
            }
        };
    }

    private void saveRole(final List<Role>roles,int idUser){
        String sqlRole="INSERT INTO user_roles (user_id,role) VALUES (?,?)";
        jdbcTemplate.batchUpdate(sqlRole, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Role role = roles.get(i);
                ps.setInt(1, idUser);
                ps.setString(2, role.name());
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });
    }
}
