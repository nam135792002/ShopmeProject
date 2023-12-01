package com.shopme.admin.category;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Category;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter extends AbstractExporter {
    public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
        super.setResponseHeaser(response,"text/csv",".csv","categories_");
        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Category ID","Category Name"};
        String[] fieldMapping = {"id","name"};
        csvBeanWriter.writeHeader(csvHeader);
        for(Category category : listCategories){
            category.setName(category.getName().replace("--","  "));
            csvBeanWriter.write(category,fieldMapping);
        }
        csvBeanWriter.close();
    }
}
