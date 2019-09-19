package ro.dobrescuandrei.autoextendstestdesktop;

import ro.dobrescuandrei.autoextends.annotations.GenerateImplementation;

@GenerateImplementation(name = "SomeAbstractClassImpl")
public abstract class SomeAbstractClass
{
    public abstract void g1();
    public abstract void g2(int x);
    public abstract SomeAbstractClass g3();
    public abstract Integer g4(SomeAbstractClass x, int y, char z);
    protected abstract String g5();
    public abstract int g6();
    public abstract char g7();
    public abstract boolean g8();
    public abstract long g9();
    protected abstract byte g10(String x, SomeAbstractClass y);
    protected abstract short g11();
    protected abstract float g12();
    protected abstract double g13();
}
