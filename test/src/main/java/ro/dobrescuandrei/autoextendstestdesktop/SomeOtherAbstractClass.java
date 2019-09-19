package ro.dobrescuandrei.autoextendstestdesktop;

import ro.dobrescuandrei.autoextends.annotations.GenerateImplementation;

@GenerateImplementation(name = "SomeOtherAbstractClassImpl")
public abstract class SomeOtherAbstractClass extends SomeAbstractClass implements SomeInterface
{
    public void h1() {}
    public abstract void h2();
}
