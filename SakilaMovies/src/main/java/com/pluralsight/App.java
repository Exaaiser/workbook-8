package com.pluralsight;

import com.pluralsight.DataManager;
import com.pluralsight.Actor;
import com.pluralsight.Film;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DataManager dm = new DataManager();

        System.out.print("Enter an actor's last name: ");
        String lastName = scanner.nextLine().trim();

        List<Actor> actors = dm.findActorsByLastName(lastName);

        if (actors.isEmpty()) {
            System.out.println("No actors found with that last name.");
            return;
        }

        System.out.println("\n--- Actors Found ---");
        for (Actor actor : actors) {
            System.out.println(actor);
        }

        System.out.print("\nEnter the actor's ID to see their movies: ");
        int actorId = Integer.parseInt(scanner.nextLine());

        List<Film> films = dm.getFilmsByActorId(actorId);

        if (films.isEmpty()) {
            System.out.println("No films found for this actor.");
        } else {
            System.out.println("\n--- Movies Featuring the Actor ---");
            for (Film film : films) {
                System.out.println(film);
                System.out.println("---------------");
            }
        }
    }
}