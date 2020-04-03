package ro.dobrescuandrei.autoextends.processor;

import java.io.Writer;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;

public abstract class CodeGenerator
{
    private CodeGenerator() {}

    public abstract void execute() throws Exception;

    public static class ClassCodeGenerator extends CodeGenerator
    {
        private final Filer filer;
        private final ElementUtils elementUtils;
        private final Element inputClass;
        private final String outputClassName;
        private final MethodCodeGenerator[] methodCodeGenerators;

        public ClassCodeGenerator(Filer filer, ElementUtils elementUtils, Element inputClass, String outputClassName, List<Element> methods)
        {
            this.filer=filer;
            this.elementUtils=elementUtils;
            this.inputClass=inputClass;
            this.outputClassName=outputClassName;
            this.methodCodeGenerators=new MethodCodeGenerator[methods.size()];

            for (int i=0; i<methods.size(); i++)
                methodCodeGenerators[i]=new MethodCodeGenerator((ExecutableElement)methods.get(i));
        }

        @Override
        public void execute() throws Exception
        {
            String packageName=elementUtils.getPackageNameOfElement(inputClass);
            String sourceCode="package "+packageName+";\n\n"+
                    "public class "+outputClassName+
                        (inputClass.getKind()==ElementKind.INTERFACE
                            ?" implements "
                            :" extends ")+
                        inputClass.toString()+"\n" +
                    "{\n";

            for (MethodCodeGenerator methodCodeGenerator : methodCodeGenerators)
            {
                methodCodeGenerator.execute();
                sourceCode+="\n\t"+methodCodeGenerator.getGeneratedSourceCode()+"\n";
            }

            sourceCode+="}";

            JavaFileObject sourceFile=filer.createSourceFile(packageName+"."+outputClassName);
            Writer writer=sourceFile.openWriter();
            writer.write(sourceCode);
            writer.close();
        }

        public class MethodCodeGenerator extends CodeGenerator
        {
            private final ExecutableElement method;
            private String code = "";

            public MethodCodeGenerator(ExecutableElement method)
            {
                this.method = method;
            }

            @Override
            public void execute()
            {
                for (Modifier modifier : method.getModifiers())
                    if (modifier!=Modifier.ABSTRACT)
                        code+=modifier.toString()+" ";

                code+=method.getReturnType().toString()+" ";
                code+=method.getSimpleName().toString()+"(";

                List<? extends VariableElement> parameters=method.getParameters();
                for (int i=0; i<parameters.size(); i++)
                {
                    VariableElement parameter=parameters.get(i);
                    code+=parameter.asType().toString()+" "+parameter.getSimpleName();

                    if (i!=parameters.size()-1)
                        code+=",";
                }

                code+=")\n\t{\n\t\t";

                String returnType=method.getReturnType().toString();
                if (returnType.equals("byte"))
                    code+="return (byte)0;";
                else if (returnType.equals("short"))
                    code+="return (short)0;";
                else if (returnType.equals("int"))
                    code+="return 0;";
                else if (returnType.equals("long"))
                    code+="return 0L;";
                else if (returnType.equals("float"))
                    code+="return 0f;";
                else if (returnType.equals("double"))
                    code+="return 0.0;";
                else if (returnType.equals("char"))
                    code+="return '\\u0000';";
                else if (returnType.equals("boolean"))
                    code+="return false;";
                else if (!returnType.equals("void"))
                    code+="return null;";

                code+="\n\t}";
            }

            public String getGeneratedSourceCode()
            {
                return code;
            }
        }
    }
}
