package ru.l3r8y;

import java.nio.file.Path;

public interface Method {

    String className();

    String name();

    String body();

    Path path();

}