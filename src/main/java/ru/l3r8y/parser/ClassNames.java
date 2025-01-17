/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023. Ivanchuck Ivan.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.l3r8y.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import ru.l3r8y.ClassName;
import ru.l3r8y.Names;

/**
 * All class names from file.
 *
 * @since 0.1.6
 */
@RequiredArgsConstructor
public final class ClassNames implements Names {

    /**
     * Path to .java file.
     */
    private final Path path;

    /**
     * The accum.
     */
    private final Collection<ClassName> accum = new ArrayList<>(0);

    @Override
    public Collection<ClassName> all() {
        try {
            StaticJavaParser.parse(this.path)
                .getChildNodes()
                .forEach(this::addIfEndsWithEr);
        } catch (final IOException ex) {
            throw new IllegalStateException(
                String.format("Unable to get class names: %s\n", ex.getMessage()),
                ex
            );
        }
        return Collections.unmodifiableCollection(this.accum);
    }

    /**
     * Addition of name which ends with "er".
     *
     * @param clazz Node from file
     */
    private void addIfEndsWithEr(final Node clazz) {
        if (clazz instanceof ClassOrInterfaceDeclaration) {
            final String name = ClassOrInterfaceDeclaration.class
                .cast(clazz)
                .getNameAsString();
            this.accum.add(new ParsedClassName(name, this.path));
        }
    }

}
