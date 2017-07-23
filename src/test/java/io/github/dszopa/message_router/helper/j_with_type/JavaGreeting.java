package io.github.dszopa.message_router.helper.j_with_type;

public class JavaGreeting {

    private String name;
    private Boolean exclamation;

    public JavaGreeting(String name, Boolean exclamation) {
        this.name = name;
        this.exclamation = exclamation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getExclamation() {
        return exclamation;
    }

    public void setExclamation(Boolean exclamation) {
        this.exclamation = exclamation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JavaGreeting)) return false;

        JavaGreeting that = (JavaGreeting) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return exclamation != null ? exclamation.equals(that.exclamation) : that.exclamation == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (exclamation != null ? exclamation.hashCode() : 0);
        return result;
    }
}
