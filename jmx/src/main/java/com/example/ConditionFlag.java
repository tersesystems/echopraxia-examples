package com.example;

class ConditionFlag {
  private final String name;
  private boolean enabled;

  public ConditionFlag(String name, boolean enabled) {
    this.name = name;
    this.enabled = enabled;
  }

  public String getName() {
    return name;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
