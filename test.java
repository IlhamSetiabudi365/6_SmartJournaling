public class test {
    public static void main(String[] args) {
        User me = new User("email", "display","pass");
        JournalPage page = new JournalPage(me);
        page.displayDates();
    }
}
