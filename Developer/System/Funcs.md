# Functions on module
### Current Time in msecs
```ysc
System->currentTime();
```
* Return number

### Get the program initialization time
```ysc
System->initTime();
```
* Return number

### Get Env variable
```ysc
System->getEnv("VarName");
```
* Return string

### Get Ysc version
```ysc
System->yscVersion();
```
* Return string

### Get Ysc version code
```ysc
System->yscVersionCode();
```
* Return number

### Get filesystem line separator
```ysc
System->lineSeparator();
```
* Return string

### Exit with program
```ysc
System->exit(0);
```
* void

