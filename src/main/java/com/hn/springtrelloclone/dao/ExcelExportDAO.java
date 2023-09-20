package com.hn.springtrelloclone.dao;

import com.hn.springtrelloclone.dto.ExcelExportDTO;

import java.util.List;

public interface ExcelExportDAO {

    List<ExcelExportDTO> getWorkDetail();
}
