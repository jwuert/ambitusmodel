package org.wuerthner.ambitus.model;

import org.junit.jupiter.api.Test;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.core.Model;
import org.wuerthner.sport.core.XmlElementReader;

import java.io.File;
import java.io.FileInputStream;

public class FileTest {
    @Test
    public void testReadWrite() {
        try {
            ModelElementFactory factory = new AmbitusFactory();
            // FileInputStream inputStream = new FileInputStream("/data/home/wuerthne/Tritonus/grosserHerr.amb");
            // FileInputStream inputStream = new FileInputStream("/home/wuerthne/todesbanden.amb");
            long start = System.nanoTime();
            FileInputStream inputStream = new FileInputStream("src/test/resources/documents/innsbruck.amb");
            XmlElementReader reader = new XmlElementReader(factory, Arrangement.TYPE);
            Arrangement root = (Arrangement) reader.run(inputStream);
            long end = System.nanoTime();

            if (root!=null)
                System.out.println(Model.makeString(root));
            else
                System.out.println(root);
            System.out.println("Loading: " + ((end-start)*0.000000001) + " seconds");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
