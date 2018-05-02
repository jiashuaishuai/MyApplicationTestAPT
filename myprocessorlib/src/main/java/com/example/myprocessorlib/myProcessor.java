package com.example.myprocessorlib;


import com.example.myannotationlib.ContentView;
import com.example.myannotationlib.Find;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class myProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private Messager messager;
    private Map<String, TypeInfor> typeMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        Types types = processingEnv.getTypeUtils();
        messager.printMessage(Diagnostic.Kind.NOTE, "初始化。。。。。");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
//        HashSet<String> set = new LinkedHashSet<>();
//        set.add(ContentView.class.getCanonicalName());
        return Collections.singleton(ContentView.class.getCanonicalName());
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "开始编译。。");

        Set<? extends Element> findElement = roundEnv.getElementsAnnotatedWith(Find.class);
        for (Element e : findElement) {
            VariableElement ve = (VariableElement) e;
            TypeElement te = (TypeElement) ve.getEnclosingElement();
            TypeInfor typeInfor = typeMap.get(te.getSimpleName().toString());
            if (typeInfor == null) {
                typeInfor = new TypeInfor(te, elementUtils);
                typeMap.put(te.getSimpleName().toString(), typeInfor);
            }
            Find find = ve.getAnnotation(Find.class);
            typeInfor.variableMap.put(find.value(), ve);

        }

        //获取以ContentView注解的类集合
        Set<? extends Element> resElement = roundEnv.getElementsAnnotatedWith(ContentView.class);
        for (Element e : resElement) {
            //转为一个类程序元素
            TypeElement typeElement = (TypeElement) e;
            //获取ContentView
            ContentView contentView = typeElement.getAnnotation(ContentView.class);

            TypeInfor typeInfor = typeMap.get(typeElement.getSimpleName().toString());
            if (typeInfor == null) {
                typeInfor = new TypeInfor(typeElement, elementUtils);
                typeMap.put(typeElement.getSimpleName().toString(), typeInfor);
            }
            typeInfor.setContentId(contentView.value());

//            //获取包名
//            String packName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
//            //类名
//            String className = typeElement.getSimpleName().toString();
//            messager.printMessage(Diagnostic.Kind.NOTE, className);
//            //获取当前类的ClassName
//            ClassName activity = ClassName.get(packName, className);
//
//            //构建方法
//            MethodSpec methodSpec = MethodSpec.methodBuilder("inject")//方法名
//                    .addAnnotation(Override.class)//注解
//                    .addModifiers(Modifier.PUBLIC)//修饰符
//                    .returns(void.class)//返回值
//                    .addParameter(activity, "host")//参数
//                    .addParameter(Object.class, "res")//参数
//                    .addStatement("host.setContentView(" + contentView.value() + ")")//具体执行代码
//                    .build();
//
//            //构建类
//            TypeSpec typeSpec = TypeSpec
//                    .classBuilder(e.getSimpleName() + "$$InjectView")//类名
//                    .addModifiers(Modifier.PUBLIC)//修饰符
//                    .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ViewInject.class), activity))//实现接口ViewInject，且指定泛型为当前注解的类
//                    .addMethod(methodSpec)//插入方法
//                    .build();
//
//
//            JavaFile file = JavaFile.builder(packName, typeSpec).build();//转为执行文件

        }
        for (String name : typeMap.keySet()) {

            try {
                TypeInfor typeInfor = typeMap.get(name);
                typeInfor.getFile().writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return false;
    }
}
