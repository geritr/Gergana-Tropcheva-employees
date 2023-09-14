import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class Gui implements ActionListener {

    static JFrame frame;
    static JPanel panel;
    JLabel label;
    static JButton button;
    static File file_path = null;
    JTable table;
    JButton yes_button;
    JButton no_button;


    /* Constructor for the user interface
     */
    public Gui(){
        frame = new JFrame();
        panel = new JPanel();
        button = new JButton("Upload File");
        yes_button = new JButton("yes");
        no_button = new JButton("no");

        panel.setBorder(BorderFactory.createEmptyBorder(50, 50,50, 50));
        panel.setLayout(new GridBagLayout());
        panel.add(button, new GridBagConstraints());

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Team Longest Period");
        frame.setSize(700, 400);
        frame.setLocationRelativeTo(null);

        button.setFocusable(false);  
        button.addActionListener(this);

        table = new JTable();

        //ImageIcon image = new ImageIcon("./icon.jpg");
        //frame.setIconImage(image.getImage());

        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception{
        new Gui();
    }


    /* actions to be performed
    * when the "Upload File" button is clicked 
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button){
            JFileChooser file_upload = new JFileChooser();
            int responce = file_upload.showOpenDialog(null);

            if(responce == JFileChooser.APPROVE_OPTION){
                file_path = new File(file_upload.getSelectedFile().getAbsolutePath());
                String file_path = file_upload.getSelectedFile().getPath();
                set_new_panel(file_path);
            }
        }
    }


    /* sets a new panel with two buttons on it - yes and no
     * as label is displayed the path of the selected file
     */
    public void set_new_panel(String file_path){
        panel.removeAll();
        panel.setLayout(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50,50, 50));

        String label_text = "Is this the file you want to select: \n" + file_path;
        System.out.println(file_path);
        JLabel label = new JLabel(label_text);

        panel.add(label);
        panel.add(yes_button);
        panel.add(no_button);

        yes_button.setFocusable(false);
        yes_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handle_yes_button();
            }
        });

        no_button.setFocusable(false);
        no_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handle_no_button();
            }
        });

        frame.validate();
        frame.repaint();      
    }


    /* if the yes button is selected 
    * process the uploaded file and determine largest period of time 
    * when a pair of employees have worked together */
    public void handle_yes_button(){
        try {
            process_file();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* if the no button is selected 
     * set new panel with the "Upload File" button
     */
    public void handle_no_button(){
        panel.removeAll();
        
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS)); // for vertical layout??
        newPanel.add(new JLabel("Upload a new file:"));
        newPanel.add(button);

        panel.setLayout(new GridBagLayout());
        panel.add(newPanel, new GridBagConstraints());

        frame.validate();
        frame.repaint();
    }


    /* goes through the input file
     * creates Employee objects with four attributes
     * for each line of the input file - new object created
     * returns a list of all Employee objects
     */
    public static ArrayList<Employee> read_csv() throws Exception {
        ArrayList<Employee> employees_list = new ArrayList<Employee>();
        Scanner read = new Scanner(file_path);
        read.useDelimiter(",|\n");

        while(read.hasNext()){

            String emp_id = read.next().trim();
            String project_id = read.next().trim();
            String date_from = read.next().trim();
            date_from = fix_format(date_from);
            String date_to = read.next().trim();
            date_to = fix_format(date_to);

            Employee emp = new Employee(emp_id, project_id, date_from, date_to);
            employees_list.add(emp);

        }

        read.close();
        return employees_list;
    }


    /* iterates through the list with all Employee objects
     * creates a list for each pair of employees that have worked together
     * consisting of the ids of both employees, project id, and days together
     */
    public void process_file() throws Exception {
        ArrayList<Employee> employees_list = read_csv();
        int current_days = 0;
        int max_days = Integer.MIN_VALUE;
        ArrayList<ArrayList<Integer>> worked_together= new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < employees_list.size() - 1; i++){
            for(int j = i+1; j < employees_list.size(); j++){
                if(employees_list.get(i).emp_id.equals(employees_list.get(j).emp_id)){
                    continue;
                }

                current_days = worked_together(employees_list.get(i), employees_list.get(j));

                if(max_days <= current_days && current_days != 0){
                    max_days = current_days;
                    ArrayList<Integer> current = new ArrayList<Integer>();
                    current.add(employees_list.get(i).get_id());
                    current.add(employees_list.get(j).get_id());
                    current.add(employees_list.get(j).get_project_id());
                    current.add(current_days);
                    worked_together.add(current);
                }
                current_days = 0;
            }
        }

        visualize_results(max_days, employees_list, worked_together);
    }


    /* iterates through the list of each pair of employees that have worked together 
     * creates a table with four columns - emp1 id, emp2 id, project id, days together
     * sets a new panel and displays the table
     */
    public void visualize_results(int max_days, ArrayList<Employee> employees_list, ArrayList<ArrayList<Integer>> worked_together){
        panel.remove(button);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Employee ID 1");
        tableModel.addColumn("Employee ID 2");
        tableModel.addColumn("Project ID");
        tableModel.addColumn("Days Worked");

        for(int j = 0; j < worked_together.size(); j++){
            if(worked_together.get(j).get(3) == max_days){

                tableModel.addRow(new Object[]{
                    worked_together.get(j).get(0),
                    worked_together.get(j).get(1),
                    worked_together.get(j).get(2),
                    worked_together.get(j).get(3)
                });
            }
        }

        table.setModel(tableModel);

        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        frame.validate();
        frame.repaint();
    }


    /* for two given Employee objects
     * determines if they have worked together on a common project
     * returns the number of days they have worked together
     */
    public static int worked_together(Employee emp1, Employee emp2){
        if(!emp1.project_id.equals(emp2.project_id)){
            return 0;
        }
        int current_days;
        LocalDate start;
        if(emp1.get_start().isBefore(emp2.get_start())){
            start = emp2.get_start();
        }else{
            start = emp1.get_start();
        }
        LocalDate end;
        if(emp1.get_end().isBefore(emp2.get_end())){
            end = emp1.get_end();
        }else{
            end = emp2.get_end();
        }
        if(end.isBefore(start)){
            return 0;
        }
        long days = ChronoUnit.DAYS.between(start, end);;
        current_days = Math.toIntExact(days);

        return current_days;
    }


    /* determines whether the dates from the input csv file 
     * are in the supported formats: yyyy/mm/dd, yyyy-mm-dd, dd/mm/yyyy, dd-mm-yyyy
    */
    public static String fix_format(String date){
        if(date.charAt(2) == '-' || date.charAt(2) == '/'){
            String[] string_split = date.split("-");
            if(string_split.length == 1){
                string_split = date.split("/");
            }
            date = "";
            for (int i = string_split.length - 1; i >= 0; i--){
                if(i!=0){date = date + string_split[i] + '-';}
                else{date = date + string_split[i];}
            }
            return date;
        }
        else if(date.split("/").length == 1){
            return date;
        }else{
            String[] string_split = date.split("/");
            date = "";
            for (int i = 0; i < string_split.length; i++){
                if(i != string_split.length - 1){date = date + string_split[i] + '-';}
                else{date = date + string_split[i];}
            }
            return date;
        }  
    }
}