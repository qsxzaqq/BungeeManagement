package cc.i9mc.bungeemanagement.admin.database;

import cc.i9mc.bungeemanagement.admin.AdminType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminData {
    private String name;
    private String displayName;
    private AdminType adminType;
}
