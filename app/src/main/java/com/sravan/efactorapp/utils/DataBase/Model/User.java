package com.sravan.efactorapp.utils.DataBase.Model;

public class User {
    private String address;
    private String email;
    private Long id;
    private String name;
    private String phone;
    private String sub_uuid;
    private Long updated_at;
    private String username;

    public String toString() {
        return "User{id=" + this.id + ", username='" + this.username + '\'' + ", sub_uuid='" + this.sub_uuid + '\'' + ", name='" + this.name + '\'' + ", updated_at=" + this.updated_at + ", address='" + this.address + '\'' + ", email='" + this.email + '\'' + ", phone='" + this.phone + '\'' + '}';
    }

    public User(Long id2, String username2, String sub_uuid2, String name2, Long updated_at2, String address2, String email2, String phone2) {
        this.id = id2;
        this.username = username2;
        this.sub_uuid = sub_uuid2;
        this.name = name2;
        this.updated_at = updated_at2;
        this.address = address2;
        this.email = email2;
        this.phone = phone2;
    }

    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id2) {
        this.id = id2;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username2) {
        this.username = username2;
    }

    public String getSub_uuid() {
        return this.sub_uuid;
    }

    public void setSub_uuid(String sub_uuid2) {
        this.sub_uuid = sub_uuid2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public Long getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(Long updated_at2) {
        this.updated_at = updated_at2;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address2) {
        this.address = address2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }
}
