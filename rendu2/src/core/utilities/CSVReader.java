package core.utilities;

import core.model.Biome;
import core.model.Domino;
import core.model.Tile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CSVReader
{
    
    private static CSVReader INSTANCE = null;

    private CSVReader(){}

    public List<Domino> generateDominos()
    {
        Domino[] tab = new Domino[48];
        try
        {
            InputStream fileStream = this.getClass().getResourceAsStream("/dominos.csv");
            assert fileStream != null;
            Scanner reader = new Scanner(fileStream).useDelimiter("\\A");
            int i=0;
            while (reader.hasNextLine())
            {
                String line = reader.nextLine();
                String[] split = line.split(",");

                int id = Integer.parseInt(split[0]);
                Biome biome = Biome.parse(split[1]);
                int crowns = Integer.parseInt(split[5]);
                Tile t1 = new Tile(biome, crowns);

                biome = Biome.parse(split[3]);
                crowns = Integer.parseInt(split[6]);
                Tile t2 = new Tile(biome, crowns);
                tab[i] = new Domino(id, t1, t2);
                i++;
            }
            reader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>(Arrays.asList(tab));
    }

    public static CSVReader getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new CSVReader();
        return INSTANCE;
    }
}
