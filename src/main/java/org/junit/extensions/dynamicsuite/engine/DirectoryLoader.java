package org.junit.extensions.dynamicsuite.engine;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Chris
 * Date: 02.07.11
 * Time: 16:26
 */
public class DirectoryLoader {

    public static final String CLASS_ENDING = ".class";

    private final File basePath;

    private final List<String> foundClassNames = new ArrayList<String>();

    public DirectoryLoader(File basePath) {
        this.basePath = basePath;
        recursiveAdd(basePath);
    }

    public List<String> getClassNames() {
        return foundClassNames;
    }

    protected void recursiveAdd(File current) {
        if (current.isDirectory()) {
            File[] files = current.listFiles();
            for (File file : files) {
                recursiveAdd(file);
            }
        } else {
            String basePathName = basePath.getAbsolutePath();
            String className = current.getAbsolutePath();
            className = extractClassName(basePathName, className);
            foundClassNames.add(className);
        }
    }

    protected String extractClassName(String basePath, String classFileName) {
        String className = classFileName;
        className = StringUtils.removeStart(className, basePath);
        className = StringUtils.removeStart(className, File.separator);
        className = StringUtils.removeEnd(className, CLASS_ENDING);
        className = StringUtils.replace(className, File.separator, ".");
        return className;
    }




}
