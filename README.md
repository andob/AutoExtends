# AutoExtends

### Java annotation processor that generates empty interface / abstract class implementations

#### 1. Import

```
allprojects {
    repositories {
        maven { url 'http://maven.andob.info/repository/open_source' }
    }
}
```
```
dependencies {
    implementation 'ro.andob.autoextends:annotations:1.0.3'
    annotationProcessor 'ro.andob.autoextends:processor:1.0.3'
}
```

#### 2. Annotate your abstract class / interface with ``@GenerateImplementation``

```java
@GenerateImplementation(name = "SomeInterfaceImpl")
public interface SomeInterface
{
    void f1();
    int f2(int x);
    List<Point> f3(Point x, Point y);
}
```

#### 3. Hit build! The annotation processor will generate the following class:

```java
public class SomeInterfaceImpl implements SomeInterface
{
    @Override
    public void f1()
    {
    }

    @Override
    public int f2(int x)
    {
        return 0;
    }

    @Override
    public List<Point> f3(Point x, Point y)
    {
        return null;
    }
}
```

### License

```java
Copyright 2019-2020 Andrei Dobrescu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.`
```
