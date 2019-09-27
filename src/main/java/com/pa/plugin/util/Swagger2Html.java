package com.pa.plugin.util;

import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import org.asciidoctor.*;
import org.asciidoctor.jruby.internal.JRubyAsciidoctor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * @author puan
 * @date 2019-06-14 16:16
 **/
public class Swagger2Html {

    public static void main(String[] args) throws Exception {
        String url = "http://47.95.118.118:9092/v2/api-docs";
        generateAsciiDocsToFile(url);
        convertAsciiDocsToHtml("C:\\Users\\Administrator\\Desktop\\all.html");
    }

    /**
     * 生成.adoc文件
     *
     * @throws Exception
     */
    public static void generateAsciiDocsToFile(String url) {
        //    输出Ascii到单文件
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
                .withOutputLanguage(Language.ZH)
                .withPathsGroupedBy(GroupBy.TAGS)
                .withGeneratedExamples()
                .withoutInlineSchema()
                .build();

        try {
            Swagger2MarkupConverter.from(new URL(url))
                    .withConfig(config)
                    .build()
                    .toFile(Paths.get("./adoc/all"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void convertAsciiDocsToHtml(String path) {
        Asciidoctor asciidoctor = JRubyAsciidoctor.create();
        asciidoctor.convertFile(new File("./adoc/all.adoc"), options(path));
    }

    /**
     * 可选builder
     *
     * @param path
     */
    private static Options options(String path) {
        return OptionsBuilder.options()
                .backend("html")//生成HTML
                .safe(SafeMode.UNSAFE)
                .headerFooter(true)
                .mkDirs(true)
                .toFile(new File(path))//输出路径
                .attributes(attributesOnBuilder())
                .get();
    }

    /**
     * 可选属性
     */
    private static Attributes attributesOnBuilder() {
        return AttributesBuilder.attributes()
                .sourceHighlighter("coderay")
                .imagesDir("images@")
                .attributeMissing("skip")
                .attributeUndefined("drop-line")
                .attribute("doctype", "book")
                .attribute("toc", "left")
                .attribute("toclevels", "3")
                .get();
    }

}
