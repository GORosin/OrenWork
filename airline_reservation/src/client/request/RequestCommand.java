package client.request;

public interface RequestCommand {
    void execute();
    void undo();
}
