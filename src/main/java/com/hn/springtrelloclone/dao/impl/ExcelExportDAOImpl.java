package com.hn.springtrelloclone.dao.impl;

import com.hn.springtrelloclone.dao.ExcelExportDAO;
import com.hn.springtrelloclone.dto.ExcelExportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExcelExportDAOImpl implements ExcelExportDAO {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<ExcelExportDTO> getWorkDetail(@Param("userId") Long userId) {
        String querySQL = "select distinct \n" +
                "u.user_id, b.board_name,l.list_name,c.card_name,u.first_name,u.last_name,u.username,u.email,u.account_info \n" +
                "                from gboard b \n" +
                "                join glist l on l.board_board_id = b.board_id \n" +
                "                join gcard c on c.list_list_id = l.list_id \n" +
                "                join gomito_users u on u.board_board_id = b.board_id \n" +
                "where u.user_id = :userId ";

        try {
            List<ExcelExportDTO> result = new ArrayList<>();
            List<Object[]> rows = entityManager.createNativeQuery(querySQL).setParameter("userId", userId).getResultList();
            for (Object[] row : rows) {
                result.add(new ExcelExportDTO((BigInteger) row[0],
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        (String) row[4],
                        (String) row[5],
                        (String) row[6],
                        (String) row[7],
                        (String) row[8]
                ));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}