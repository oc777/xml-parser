# XML parser

**Java application for parsing txt file into xml.**

- The input format is line-based. 
- The output format is XML. 
- The user(s) is unknown at the moment as well as the need of further requirements.



## Input

```
P|firstname|lastname 

T|mobile|fixed number 

A|street|city|postal code 

F|name|year of birth
````

- P can be followed by T, A and F 
- F can be followed by T och A

## Output

```
<people>
  <person>
    <firstname>Carl Gustaf</firstname> 
    <lastname>Bernadotte</lastname>
    <address>
      <street>Drottningholms slott</street> 
      ...
    </address>
    <phone>
      <mobile>0768-101801</mobile> 
      ...
    </phone>
    <family>
      <name>Victoria</name> 
      <born>1977</born> 
      <address>...</address>
    </family>
    <family>...</family> 
  </person>
  <person>...</person> 
</people>
```
