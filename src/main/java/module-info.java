module utec {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.boot.starter.web;
    requires spring.boot.starter.data.jpa;
    requires spring.boot.starter.security;
    requires java.sql;
    requires lombok;
    requires java.validation;
    requires org.hibernate.validator;
    requires java.persistence;
    requires spring.beans;
    requires mysql.connector.j;
    requires spring.boot.starter.test;
    requires spring.context;
    requires spring.data.jpa;
    requires spring.web;
    requires spring.security.crypto;
    requires spring.security.config;
    requires java.annotation;
    requires spring.tx;
    requires spring.security.core;
    requires java.desktop;
    requires org.hibernate.orm.core;
    requires spring.data.commons;
    requires org.apache.pdfbox;
    opens utec to javafx.fxml;
    exports utec.gestorApp.ui;
    exports utec.gestorApp.usuario;
    exports utec;
    exports utec.gestorApp.evento;
}