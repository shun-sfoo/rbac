package com.matrix.rbac.controller.bus;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.matrix.rbac.common.JsonResult;
import com.matrix.rbac.model.dao.FileRecordDao;
import com.matrix.rbac.model.dao.InfoDao;
import com.matrix.rbac.model.entity.FileRecord;
import com.matrix.rbac.model.entity.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/bus/info")
@Slf4j
@Transactional(readOnly = true)
public class InfoController {

    @Autowired
    private InfoDao infoDao;

    @Autowired
    private FileRecordDao fileRecordDao;

    @RequestMapping
    public void index() {
    }

    @RequestMapping("/load")
    public String form(Long id, Model model) {
        model.addAttribute("info", infoDao.findById(id).get());
        return "bus/info/form";
    }

    @PostMapping("/update")
    @ResponseBody
    @Transactional
    public JsonResult save(Info info) {
        infoDao.save(info);
        return JsonResult.success();
    }

    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int rows, Info info, String startTime, String endTime) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PageRequest pr = PageRequest.of(page - 1, rows, Sort.Direction.DESC, "salesDate");
        Specification<Info> spec = (root, query, cb) -> {
            Predicate predicates = cb.conjunction();
            if (StringUtils.hasText(info.getCustomer())) {
                predicates.getExpressions().add(cb.like(root.get("customer"), "%" + info.getCustomer() + "%"));
            }

            if (StringUtils.hasText(info.getProductCode())) {
                predicates.getExpressions().add(cb.like(root.get("productCode"), "%" + info.getProductCode() + "%"));
            }

            if (StringUtils.hasText(info.getProductName())) {
                predicates.getExpressions().add(cb.like(root.get("productName"), "%" + info.getProductName() + "%"));
            }

            if (StringUtils.hasText(startTime)) {
                predicates.getExpressions().add(cb.greaterThanOrEqualTo(root.get("salesDate").as(String.class), startTime));
            }

            if (StringUtils.hasText(endTime)) {
                String tmp = endTime + " 23:59:59";
                predicates.getExpressions().add(cb.lessThanOrEqualTo(root.get("salesDate").as(String.class), tmp));
            }

            return predicates;
        };
        Page<Info> pageData = infoDao.findAll(spec, pr);
        Map<String, Object> data = new HashMap<>();
        data.put("total", pageData.getTotalElements());
        data.put("rows", pageData.getContent());
        return data;
    }

    @PostMapping(value = "/upload")
    @ResponseBody
    @Transactional
    public JsonResult upload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            String name = multipartFile.getOriginalFilename();
            FileRecord fr = fileRecordDao.findByFileName(name);
            if (fr != null) {
                return JsonResult.error("已上传过相同名称的文件");
            }
            EasyExcel.read(multipartFile.getInputStream(), Info.class, new PageReadListener<Info>(dataList -> {
                infoDao.saveAll(dataList);
            })).sheet().doRead();
            FileRecord record = new FileRecord();
            record.setFileName(name);
            fileRecordDao.save(record);
            return JsonResult.success("上传成功!");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return JsonResult.error("上传异常");
        }
    }


    @GetMapping("/export")
    public void export(HttpServletResponse response, Info info, String startTime, String endTime) throws IOException {
        Specification<Info> spec = (root, query, cb) -> {
            Predicate predicates = cb.conjunction();
            if (StringUtils.hasText(info.getCustomer())) {
                predicates.getExpressions().add(cb.like(root.get("customer"), "%" + info.getCustomer() + "%"));
            }

            if (StringUtils.hasText(info.getProductCode())) {
                predicates.getExpressions().add(cb.like(root.get("productCode"), "%" + info.getProductCode() + "%"));
            }

            if (StringUtils.hasText(info.getProductName())) {
                predicates.getExpressions().add(cb.like(root.get("productName"), "%" + info.getProductName() + "%"));
            }

            if (StringUtils.hasText(startTime)) {
                predicates.getExpressions().add(cb.greaterThanOrEqualTo(root.get("salesDate").as(String.class), startTime));
            }

            if (StringUtils.hasText(endTime)) {
                String tmp = endTime + " 23:59:59";
                predicates.getExpressions().add(cb.lessThanOrEqualTo(root.get("salesDate").as(String.class), tmp));
            }

            return predicates;
        };
        List<Info> list = infoDao.findAll(spec, Sort.by(Sort.Direction.DESC, "salesDate"));
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short)10);
        headWriteCellStyle.setWriteFont(headWriteFont);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, (List<WriteCellStyle>) null);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), Info.class).registerWriteHandler(horizontalCellStyleStrategy).sheet("流向数据").doWrite(list);
    }

    @GetMapping("/delete")
    @ResponseBody
    @Transactional
    public JsonResult delete(Long id) {
        if (infoDao.findById(id).isPresent()) {
            infoDao.deleteById(id);
            return JsonResult.success();
        }
        return JsonResult.error("数据不存在!");
    }

    @PostMapping("/multiple")
    @ResponseBody
    @Transactional
    public JsonResult multiple(Long[] id) {
        infoDao.deleteAllById(Arrays.asList(id));
        return JsonResult.success("删除成功");
    }
}
