/**                                                                           
 * Created by Bruno Patini Furtado [http://bpfurtado.livejournal.com]         
 * Created on 02/07/2009 19:15:32
 *                                                                            
 * This file is part of the Text Adventures Suite.                            
 *                                                                            
 * Text Adventures Suite is free software: you can redistribute it and/or modify          
 * it under the terms of the GNU General Public License as published by       
 * the Free Software Foundation, either version 3 of the License, or          
 * (at your option) any later version.                                        
 *                                                                            
 * Text Adventures Suite is distributed in the hope that it will be useful,               
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              
 * GNU General Public License for more details.                               
 *                                                                            
 * You should have received a copy of the GNU General Public License          
 * along with Text Adventures Suite.  If not, see <http://www.gnu.org/licenses/>.         
 *                                                                            
 * Project page: http://code.google.com/p/text-adventures-suite/              
 */

package net.bpfurtado.tas.runner.savegame;

import net.bpfurtado.tas.model.Game;
import net.bpfurtado.tas.model.Scene;

public interface SaveGameListener
{
    void fireOpenSavedGameEvent(SaveGame saveGame);

    Game gameFrom(SaveGame saveGame);

    void openScene(Scene to, boolean execActions);

    void log(String msg);
}