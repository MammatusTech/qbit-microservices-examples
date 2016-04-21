package com.mammatustech.todo;

public class Todo {

    private  String id;

    private final String name;
    private final String description;
    private final long createTime;

    public Todo(String name, String description, long createTime) {
        this.name = name;
        this.description = description;
        this.createTime = createTime;

        this.id = name + "::" + createTime;
    }


    public String getId() {
        if (id == null) {
            this.id = name + "::" + createTime;
        }
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getCreateTime() {
        return createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Todo todo = (Todo) o;

        if (createTime != todo.createTime) return false;
        return !(name != null ? !name.equals(todo.name) : todo.name != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) (createTime ^ (createTime >>> 32));
        return result;
    }
}
