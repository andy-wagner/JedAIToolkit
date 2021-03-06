/*
* Copyright [2016] [George Papadakis (gpapadis@yahoo.gr)]
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
 */
package BlockBuilding;

import Utilities.DataStructures.AbstractDuplicatePropagation;
import Utilities.DataStructures.UnilateralDuplicatePropagation;
import DataModel.AbstractBlock;
import DataModel.EntityProfile;
import DataReader.EntityReader.IEntityReader;
import DataReader.EntityReader.EntitySerializationReader;
import DataReader.GroundTruthReader.GtSerializationReader;
import DataReader.GroundTruthReader.IGroundTruthReader;
import Utilities.Enumerations.BlockBuildingMethod;
import Utilities.BlocksPerformance;
import java.util.List;

/**
 *
 * @author G.A.P. II
 */
public class TestAllMethodsDirtyER {

    public static void main(String[] args) {
        String mainDirectory = "D:\\Data\\newDatasetFormat\\dirtyERfiles\\";
        String[] datasets = {"abtBuy", "amazonGp", "cddb", "census", "cora", "dblpAcm", "dblpScholar", "movies", "restaurant"};
        for (String dataset : datasets) {
            System.out.println("\n\n\n\nCurrent dataset\t:\t" + dataset);
            
            IEntityReader eReader = new EntitySerializationReader(mainDirectory + dataset + "Profiles");
            List<EntityProfile> profiles = eReader.getEntityProfiles();
            System.out.println("Input Entity Profiles\t:\t" + profiles.size());

            IGroundTruthReader gtReader = new GtSerializationReader(mainDirectory + dataset + "IdDuplicates");
            final AbstractDuplicatePropagation duplicatePropagation = new UnilateralDuplicatePropagation(gtReader.getDuplicatePairs(eReader.getEntityProfiles()));
            System.out.println("Existing Duplicates\t:\t" + duplicatePropagation.getDuplicates().size());

            for (BlockBuildingMethod blbuMethod : BlockBuildingMethod.values()) {
                double time1 = System.currentTimeMillis();

                System.out.println("\n\nCurrent blocking metohd\t:\t" + blbuMethod);
                IBlockBuilding blockBuildingMethod = BlockBuildingMethod.getDefaultConfiguration(blbuMethod);

                System.out.println("Block Building...");
                List<AbstractBlock> blocks = blockBuildingMethod.getBlocks(profiles, null);
                double time2 = System.currentTimeMillis();

                BlocksPerformance blStats = new BlocksPerformance(blocks, duplicatePropagation);
                blStats.setStatistics();
                blStats.printStatistics(time2 - time1, blockBuildingMethod.getMethodConfiguration(), blockBuildingMethod.getMethodName());
            }
        }
    }
}
