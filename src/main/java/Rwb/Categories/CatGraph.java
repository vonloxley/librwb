/*
 * Copyright (C) 2014 Niki Hansche
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Rwb.Categories;


import Rwb.Wiki;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.security.auth.login.FailedLoginException;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

/**
 *
 * @author Niki Hansche
 */
public class CatGraph {

    static Wiki rwiki;

    public static void addEdges(String parent, DirectedGraph<String, DefaultEdge> dg) throws IOException {
        List<String> c = Arrays.asList(rwiki.getCategoryMembers(parent, new int[]{14}));
        for (String cat : c) {
            dg.addVertex(cat);
            dg.addEdge(cat, parent);
            addEdges(cat, dg);
        }
    }

    public static DirectedGraph<String, DefaultEdge> buildcatgraph(String category) throws IOException {
        DirectedGraph<String, DefaultEdge> dg = new DefaultDirectedGraph<>(DefaultEdge.class);
        dg.addVertex(category);
        addEdges(category, dg);

        return dg;
    }

    public static void main(String[] args) throws IOException, FailedLoginException {
        rwiki = new Wiki("kochwiki.org", "/w");
        DirectedGraph<String, DefaultEdge> dg = buildcatgraph("Kategorie:Vegetarische Rezepte");

        showGraph(dg);

        System.out.println(dg);
        System.out.println(dg.incomingEdgesOf("Kategorie:Vegane Suppen"));
        System.out.println(dg.outgoingEdgesOf("Kategorie:Vegane Suppen"));

    }

    public static void showGraph(DirectedGraph<String, DefaultEdge> dg) throws HeadlessException {
        final JGraphXAdapter graph = new JGraphXAdapter(dg);
        graph.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");
        //mxFastOrganicLayout layout = new mxFastOrganicLayout(graph);
        //layout.setForceConstant(150);
        mxGraphLayout layout = new mxHierarchicalLayout(graph, SwingConstants.WEST);

        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setVisible(true);
        frame.getContentPane().add(BorderLayout.CENTER, graphComponent);
        graph.getModel().beginUpdate();
        try {
            layout.execute(graph.getDefaultParent());
        } finally {
            graph.getModel().endUpdate();
            /*mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);

            morph.addListener(mxEvent.DONE, new mxIEventListener() {

                @Override
                public void invoke(Object arg0, mxEventObject arg1) {
                    graph.getModel().endUpdate();
                    //fitViewport();
                }

            });

            morph.startAnimation();*/
        }
    }

}
