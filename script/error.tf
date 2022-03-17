# https://marketplace.visualstudio.com/items?itemName=twineworks.tweakflow
# https://twineworks.github.io/tweakflow/reference.html
# https://twineworks.github.io/tweakflow/modules/std.html#std

import * as std from "std";

# local alias for imported library
alias std.strings as str;

library echopraxia {

  function evaluate: (string level, dict ctx) ->
    let {
      find_string: ctx[:find_string];
    }
    (str.lower_case(find_string("$.exception.message")) == "some message");
    
}
