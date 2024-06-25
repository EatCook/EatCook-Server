package com.itcook.cooking.domain.domains.user.domain.repository;

import com.itcook.cooking.domain.domains.user.domain.entity.UserCookingTheme;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserCookingThemeJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<UserCookingTheme> userCookingThemes, Long userId) {
        jdbcTemplate.batchUpdate(
            "insert into user_cooking_theme (cooking_type, user_id) values (?,?)",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, userCookingThemes.get(i).getCookingType().name());
                    ps.setLong(2, userId);
                }

                @Override
                public int getBatchSize() {
                    return userCookingThemes.size();
                }
            }
        );
    }

}
