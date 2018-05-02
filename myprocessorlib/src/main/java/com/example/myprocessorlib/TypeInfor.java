package com.example.myprocessorlib;

import com.example.injectapi.ViewInject;
import com.example.myannotationlib.ContentView;
import com.example.myannotationlib.Find;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by JiaShuai on 2018/4/28.
 */

public class TypeInfor {

    public Map<Integer, VariableElement> variableMap = new HashMap<>();
    private TypeElement typeElement;
    private Elements utils;
    private String packageName;
    private String className;
    private ClassName hostClass;
    private int contentId;

    void setContentId(int contentId) {
        this.contentId = contentId;
    }

    TypeInfor(TypeElement typeElement, Elements utils) {
        this.typeElement = typeElement;
        this.utils = utils;
        packageName = utils.getPackageOf(typeElement).getQualifiedName().toString();
        className = typeElement.getSimpleName().toString();
        hostClass = ClassName.get(packageName, className);
        ContentView cv = typeElement.getAnnotation(ContentView.class);
        contentId = cv.value();
    }

    JavaFile getFile() {

        //创建方法
        MethodSpec.Builder method = MethodSpec
                .methodBuilder("inject")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(hostClass, "host")
                .addParameter(Object.class, "res")
                .addStatement("host.setContentView(" + contentId + ")");

        //添加其他执行代码
        addStatement(method);
        //创建类
        TypeSpec typeSpec = TypeSpec
                .classBuilder(className + "$$InjectView")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ViewInject.class), hostClass))
                .addMethod(method.build())
                .build();


        return JavaFile.builder(packageName, typeSpec).build();
    }

    private void addStatement(MethodSpec.Builder methodSpec) {
        for (int id : variableMap.keySet()) {
            VariableElement ve = variableMap.get(id);
            String veName = ve.getSimpleName().toString();

            methodSpec.addStatement("host." + veName + " = (" + ve.asType().toString() + ")((android.view.View)res).findViewById(" + id + ")");
        }


    }
}
