package ro.dobrescuandrei.autoextends.processor;

import com.google.auto.service.AutoService;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import ro.dobrescuandrei.autoextends.annotations.GenerateImplementation;

@AutoService(Processor.class)
public class AutoExtendsProcessor extends AbstractProcessor
{
    private ElementUtils elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        super.init(processingEnv);
        Types typeUtils=processingEnv.getTypeUtils();
        Elements standardElementUtils=processingEnv.getElementUtils();
        elementUtils=new ElementUtils(typeUtils, standardElementUtils);
        filer=processingEnv.getFiler();
        messager=processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(GenerateImplementation.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment)
    {
        try
        {
            List<CodeGenerator> codeGenerators=new LinkedList<>();

            for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(GenerateImplementation.class))
            {
                if (annotatedElement.getKind()!=ElementKind.CLASS&&annotatedElement.getKind()!=ElementKind.INTERFACE)
                    throw new ProcessingException("Only classes and interfaces can be annotated with "+GenerateImplementation.class.getSimpleName(), annotatedElement);

                String implementationClassName=annotatedElement.getAnnotationsByType(GenerateImplementation.class)[0].name();
                List<Element> targetMethods=new LinkedList<>();

                for (Element enclosedElement : elementUtils.getOwnedAndInheritedEnclosedElements(annotatedElement))
                {
                    if (enclosedElement.getKind()==ElementKind.METHOD)
                    {
                        boolean isAbstract=enclosedElement.getModifiers().stream().anyMatch(modifier -> modifier==Modifier.ABSTRACT);
                        boolean isPublic=enclosedElement.getModifiers().stream().anyMatch(modifier -> modifier==Modifier.PUBLIC);
                        boolean isProtected=enclosedElement.getModifiers().stream().anyMatch(modifier -> modifier==Modifier.PROTECTED);
                        if (isAbstract&&(isPublic||isProtected))
                            targetMethods.add(enclosedElement);
                    }
                }

                codeGenerators.add(new CodeGenerator.ClassCodeGenerator(
                        /*filer=*/ filer,
                        /*elementUtils=*/ elementUtils,
                        /*inputClass=*/ annotatedElement,
                        /*outputClassName=*/ implementationClassName,
                        /*inputClassMethods=*/ targetMethods));
            }

            for (CodeGenerator codeGenerator : codeGenerators)
                codeGenerator.execute();
        }
        catch (ProcessingException ex)
        {
            messager.printMessage(Diagnostic.Kind.ERROR, ex.getMessage(), ex.element);
        }
        catch (Throwable ex)
        {
            messager.printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
        }

        return true;
    }
}
