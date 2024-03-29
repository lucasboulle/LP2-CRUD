package com.business.employee;

import com.business.RegistrableObject;
import com.commons.enums.Actions;
import com.commons.enums.Role;
import com.dao.EmployeeRepository;
import com.dao.LogRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class Employee extends RegistrableObject {

    public Employee(String user, String password, String name, Role role) {
        if(user.isEmpty() || name.isEmpty() || password.isEmpty())
            throw new IllegalArgumentException("Value cannot be empty.");

        this.user = user;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    @Override
    public void register(RegistrableObject obj) {
        EmployeeRepository repository = new EmployeeRepository();
        repository.registerEmployee((Employee) obj);
    }

    public void registerObject(RegistrableObject obj) throws IOException {
        String className = obj.getClass().getSimpleName();
        System.out.println(className);
        boolean hasPermission = Arrays.asList(role.getActions()).contains(Actions.valueOf(
                obj.getClass().getSimpleName()
        ));
        if (hasPermission) {
            obj.register(obj);
            LogRepository.registerLog(obj.getClass().getSimpleName() + "|" + user + "|" + new Date());
        } else {
            throw new IllegalArgumentException("Permissão de usuário negada.");
        }
    }

    private boolean isManager() {
        return role == Role.Manager;
    }

    @Override
    public String toString() {
        return user + "|" + password + "|" + name + "|" + isManager();
    }

    private Role role;
    private String name;
    private String user;
    private String password;
}
