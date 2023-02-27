package com.softura.skillup.annotation;

import org.junit.jupiter.api.Test;

import javax.tools.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

class GenerateNotNullProcessorTest {
    @Test
    public void runAnnoationProcessor() throws Exception {
        String source = "C:/Users/radhikaa/Documents/Radhika/Learning/test-exceptiom/skillup";

        Iterable<JavaFileObject> files = getSourceFiles(source);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        JavaCompiler.CompilationTask task = compiler.getTask(new PrintWriter(System.out), null, null, null, null, files);
        task.setProcessors(Arrays.asList(new GenerateNotNullProcessor()));

        task.call();
    }

    private Iterable<JavaFileObject> getSourceFiles(String p_path) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager files = compiler.getStandardFileManager(null, null, null);

        files.setLocation(StandardLocation.SOURCE_PATH, Arrays.asList(new File(p_path)));

        Set<JavaFileObject.Kind> fileKinds = Collections.singleton(JavaFileObject.Kind.SOURCE);
        return files.list(StandardLocation.SOURCE_PATH, "", fileKinds, true);
    }
}