package com.mycompany.ejercicio1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rubén
 */
public class TaskManager {
    private String fileName;
    
    /**
     * Constructor for class TaskManager
     * 
     * @param fileName the name of the file that the manager is going to use.
     */
    public TaskManager(String fileName){
        this.fileName = fileName;
    }

    /**
     * Getter of fileName
     * 
     * @return the name of the file being used.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Setter of fileName
     * 
     * @param fileName the name of the file that is going to be used.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Checks if the given task is already defined in the current file.
     * 
     * @param task the name of the task that is going to be searched in the file.
     * @return true if the given task has been already defined or false if none
     * of the existing tasks match the given one.
     */
    public boolean existsTask(String task){
        try {
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            
            while(line != null){
                if(task.compareTo(line) == 0){
                    return true;
                }
                line = reader.readLine();
            }
            return false;
        } catch (Exception ex) {
            Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * Adds the given task to the current file, checking if it has been previously
     * defined.
     * 
     * @param task the name of the task that is going to be written in the file.
     * @return true if the task has been succesfully written in the file or false
     * if any exception is caught or if the given task already exists in the file.
     */
    public boolean newTask(String task){
        FileWriter fw = null;
        try {
            File file = new File(fileName);
            fw = new FileWriter(file, true);
            if(existsTask(task)){
                System.out.println("Ya existe la tarea");
                return false;
            }
            fw.write(task + "\n");
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally{
            try{
                fw.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * List all the existing tasks in the file. Printing the name of the task and
     * their index. At the end it also prints the file name and the amount of tasks.
     * This method is unused in the current version, since it was planned for the
     * console version of the program.
     * 
     * @return true if the file is succesfully read and all the tasks are printed
     * or false if there's no file.
     */
    public boolean listTasks(){
        File file = new File(fileName);
        if(file.exists()){
            try{
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                int count = 1;
                while(line != null){
                    System.out.println(count + "-" + line);
                    line = reader.readLine();
                    count++;
                }
                System.out.println("==================");
                System.out.println("Archivo: " + fileName);
                System.out.println("Total de tareas: " + (count - 1));
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("No existe el archivo.");
            return false;
        }
        return true;
    }
    
    /**
     * Removes the given task from the file, rewriting the entire file again 
     * without it.
     * 
     * @param taskToDelete the name of the task that is going to be removed from
     * the file
     * @return true if it succesfully removes the given task from the file or false
     * if the task doesn't exist, if an exception is caught or if there's no file.
     */
    public boolean deleteTask(String taskToDelete){
        File file = new File(fileName);
        ArrayList<String> tasks = new ArrayList<String>();
        boolean found = false;
        if(file.exists()){
            try{
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                
                while(line != null){
                    if(taskToDelete.compareTo(line) != 0){
                        tasks.add(line);
                        found = true;
                    }
                    line = reader.readLine();    
                }
                FileWriter fw = null;
                
                try{
                    fw = new FileWriter(file);
                    
                    for(String task: tasks){
                        fw.write(task + "\n");
                    }
                    
                    if(found){
                        System.out.println("Eliminada la tarea \"" + (taskToDelete) + "\"");
                    }else{
                        System.out.println("No existe la tarea \"" + (taskToDelete) + "\"");
                        return false;
                    }
                    
                }catch(IOException e){
                    e.printStackTrace();
                    return false;
                }finally{
                    fw.close();
                }
                
            }catch(FileNotFoundException e){
                e.printStackTrace();
                return false;
            }catch(IOException e){
                System.out.println("Error en la introducción de datos.");
                return false;
            }catch(NumberFormatException e){
                System.out.println("Error en la introducción de datos.");
                return false;
            }
        }else{
            System.out.println("No existe el archivo.");
            return false;
        }
        return true;
    }
    
    /**
     * Renames the given task. It will still appear in the same position than before.
     * Checks that the new name isn't empty.
     * 
     * @param taskToModify the name of the task that is being renamed.
     * @param newName the new name of the task.
     * @return true if it succesfully renames the task or false if the given task
     * doesn't exist, if an exception is caught or if the file doesn't exist.
     */
    public boolean modifyTask(String taskToModify, String newName){
        File file = new File(fileName);
        ArrayList<String> tasks = new ArrayList<String>();
        boolean found = false;
        if(file.exists()){
            try{
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                
                if(existsTask(newName)){
                    System.out.println("Ya existe la tarea \"" + (newName) + "\"");
                    return false;
                }
                
                while(line != null){
                    if(taskToModify.compareTo(line) == 0 && !newName.isEmpty()){
                        tasks.add(newName);
                        found = true;
                    }else{
                        tasks.add(line);
                    }
                    line = reader.readLine();
                }
                
                FileWriter fw = null;
                
                try{
                    fw = new FileWriter(file);
                    
                    for(String task: tasks){
                        fw.write(task + "\n");
                    }
                    
                    if(found){
                        System.out.println("Modificada la tarea \"" + (taskToModify) + "\"");
                    }
                    
                }catch(IOException e){
                    e.printStackTrace();
                    return false;
                }finally{
                    fw.close();
                }
                
            }catch(FileNotFoundException e){
                e.printStackTrace();
                return false;
            }catch(IOException e){
                System.out.println("Error en la introducción de datos.");
                return false;
            }catch(NumberFormatException e){
                System.out.println("Error en la introducción de datos.");
                return false;
            }
        }else{
            System.out.println("No existe el archivo.");
            return false;
        }
        return true;
    }
    
    /**
     * Asks for a file name and changes the current file to the given one. If it
     * doesn't exist, a new file with the given name will be created.
     * This method is unused in the current version, since it was planned for the
     * console version of the program. 
     * 
     * @return true if the file changes succesfully or false if an exception is
     * caught
     */
    public boolean changeFile(){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Fichero actual: " + fileName);
            System.out.println("---------------");
            System.out.println("Nombre del nuevo fichero (sin extension): ");
            String newFileName = br.readLine();
            newFileName = newFileName + ".txt";
            setFileName(newFileName);
            return true;
        }catch(IOException e){
            System.out.println("Error en la introducción de datos.");
            return false;
        }
    }
}
