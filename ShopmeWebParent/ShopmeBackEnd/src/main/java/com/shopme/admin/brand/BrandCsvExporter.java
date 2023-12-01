package com.shopme.admin.brand;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Brand;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class BrandCsvExporter extends AbstractExporter {
    public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {
        super.setResponseHeaser(response,"text/csv",".csv","brands_");
        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Brand ID", "Brand Name", "Categories"};
        String[] fieldMapping = {"id", "name", "categories"};
        csvBeanWriter.writeHeader(csvHeader);
        for(Brand brand : listBrands){
            csvBeanWriter.write(brand, fieldMapping);
        }
        csvBeanWriter.close();
    }
}
