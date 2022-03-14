# Scripting

This project demonstrates how to use scripts in conditions. 

There are two scripts, `error.tf` and `info.tf`.  You can run the program, and change the conditions in the scripts while the program is running. 

The contents of `info.tf`:

```
library echopraxia {
  function evaluate: (string level, function ctx) ->
    let {
      find_number: ctx("find_number");
    }
    find_number("$.age") == 1;
}
```

The contents of `error.tf`:

```
import * as std from "std";

# local alias for imported library
alias std.strings as str;

library echopraxia {

  function evaluate: (string level, function ctx) ->
    let {
      find_string: ctx("find_string");
    }
    (str.lower_case(find_string("$.exception.message")) == "some message");
}
```

The program watches and recompiles the scripts when the files are changed, so you can change the criteria of logging on the fly.