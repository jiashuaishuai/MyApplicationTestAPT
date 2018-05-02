package com.example.jiashuai.myapplicationtestapt.dao;

/**
 * Created by JiaShuai on 2018/4/25.
 */
@Table("user")
public class Filter {
    @Column("id")
    private int id;
    @Column("name")
    private String name;
    @Column("age")
    private String age;
    @Column("email")
    private String email;
    @Column("city")
    private String city;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
