package ro.dobrescuandrei.autoextends.processor;

import javax.lang.model.element.Element;

public class ProcessingException extends RuntimeException
{
    public final Element element;

    public ProcessingException(String message, Element element)
    {
        super(message);
        this.element=element;
    }
}
