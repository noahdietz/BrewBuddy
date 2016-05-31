package edu.calpoly.ndietz.brewbuddy;

/**
 * Created by ndietz on 4/17/16.
 */
public class BrewMethod {
    protected String m_method_name;

    public BrewMethod(String name) {
        this.m_method_name = name;
    }

    public BrewMethod() {
        this.m_method_name = "";
    }

    public String getM_method_name() {
        return this.m_method_name;
    }

    public void setM_method_name(String name) {
        this.m_method_name = name;
    }

    @Override
    public String toString() {
        return this.m_method_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BrewMethod) {
            BrewMethod tmp = (BrewMethod)obj;

            return this.m_method_name.equals(tmp.getM_method_name());
        } else {
            return false;
        }
    }
}
