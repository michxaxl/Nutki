public class Controller {

    private Keyboard keyboard;
    private Settings settings;
    private App app;

    private Thread thread;


    public Controller(Keyboard keyboard, Settings settings, App app, Thread thread) {
        this.keyboard = keyboard;
        this.settings = settings;
        this.app = app;
        this.thread = thread;


    }


}
