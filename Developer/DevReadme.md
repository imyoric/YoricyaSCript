# Docs for ysc developing

### Space
Default function on space: `func main{}`

<b>Space cannot support running code than:</b>

* Mark file as a <u>class</u> <i>(must be specified at the beginning of the .ysc file!)</i>
```ysc
class ClassName;
```

* Mark file as a <u>module</u> <i>(must be specified at the beginning of the .ysc file!)</i>
```ysc
module ModuleName;
```

* Import modules:
```ysc
import System;
```

* Global vars initialization:
```ysc
var a = 1;
var b = "Aboba";
```

* Functions initialization:
```ysc
func main{
  println("Hello World!");
}
```

[More docs](/Developer/Spaces.md)

___
### Functions
<b>Functions cannot support import modules, init other functions</b>

* Function body:
```ysc
func main{
  println("Hello World!");
}
```

* Any functions contains `this` object and `this.args` array:
```ysc
func main{
  println(this.args.length);
  println(this.args.0); //or println(this.args.[0]);
}
```

* Call to function:
```ysc
func main{
  test("arg0", "arg1", "arg2");
}

func test{
  println(this.args.0); //or println(this.args.[0]);
}
```
[Internal default function](/Developer/iFunctions.md)
[More docs](/Developer/Functions.md)

___
### Variables
<b>Variables init and change always via `var` initializer</b>

* init or change variable:
```ysc
    var a = 2;

    //via function in module:

    var a = System->currentTime();
```

* multiply number:
```ysc
    var a *= 2;
```

* divide number:
```ysc
    var a /= 2;
```

* Add to number:
```ysc
    var a += 2;
```

* Subtract from number:
```ysc
    var a -= 2;
```

[More docs](/Developer/Vars.md)

___
### Modules
<b>Call to functions in modules only by `->`</b>

* importing and using:
```ysc
import System;

func main{
  println("System time: $System->currentTime()$");
}
```

[Internal Modules](/Developer/internalModules.md)
[More docs](/Developer/Modules.md)

___
### Classes
<b>Call to class element only by `.`</b>

* Creating and using:
```ysc
func main{
  var a = new("ClassName");
  a.functionOnClass();
  var a.varOnClass = 1;
}
```

[Internal Classes](/Developer/internalClasses.md)
[More docs](/Developer/Classes.md)
