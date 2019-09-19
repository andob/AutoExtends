package ro.dobrescuandrei.autoextends.processor;

import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class ElementUtils
{
    private Types typeUtils;
    private Elements standardElementUtils;

    public ElementUtils(Types typeUtils, Elements standardElementUtils)
    {
        this.typeUtils = typeUtils;
        this.standardElementUtils = standardElementUtils;
    }

    public List<Element> getOwnedAndInheritedEnclosedElements(Element element)
    {
        List<Element> ownedAndInheritedElements=new LinkedList<>();
        ownedAndInheritedElements.addAll(element.getEnclosedElements());
        ownedAndInheritedElements.addAll(getInheritedEnclosedElements(element));
        return ownedAndInheritedElements;
    }

    public List<Element> getInheritedEnclosedElements(Element element)
    {
        List<Element> inheritedElements=new LinkedList<>();

        for (TypeMirror supertype : typeUtils.directSupertypes(element.asType()))
        {
            Element superClassElement=((DeclaredType)supertype).asElement();
            String superClassName=superClassElement.toString();
            if (!superClassName.startsWith("java.")&&
                !superClassName.startsWith("javax.")&&
                !superClassName.startsWith("android.")&&
                !superClassName.startsWith("androidx."))
            {
                inheritedElements.addAll(superClassElement.getEnclosedElements());
                inheritedElements.addAll(getInheritedEnclosedElements(superClassElement));
            }
        }

        return inheritedElements;
    }

    public String getPackageNameOfElement(Element element)
    {
        return standardElementUtils.getPackageOf(element).toString();
    }
}
