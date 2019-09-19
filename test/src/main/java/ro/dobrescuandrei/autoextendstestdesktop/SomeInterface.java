package ro.dobrescuandrei.autoextendstestdesktop;

import ro.dobrescuandrei.autoextends.annotations.GenerateImplementation;

@GenerateImplementation(name = "SomeInterfaceImpl")
public interface SomeInterface
{
    void f1();
    void f2(int x);
    SomeAbstractClass f3();
    Integer f4(SomeAbstractClass x, int y, char z);
    String f5();
    int f6();
    char f7();
    boolean f8();
    long f9();
    byte f10(String x, SomeAbstractClass y);
    short f11();
    float f12();
    double f13();
}
