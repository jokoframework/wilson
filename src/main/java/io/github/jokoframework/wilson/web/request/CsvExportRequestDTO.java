/**
 * 
 */
package io.github.jokoframework.wilson.web.request;

import java.util.List;

/**
 * @author bsandoval
 *
 */
public class CsvExportRequestDTO {
    
    private List<String> columns;
    
    public List<String> getColumns() {
        return columns;
    }
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
