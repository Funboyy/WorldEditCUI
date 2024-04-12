package org.enginehub.worldeditcui.render.region;

import de.funboyy.addon.worldedit.cui.api.render.RenderContext;
import java.util.ArrayList;
import java.util.List;
import org.enginehub.worldeditcui.WorldEdit;
import org.enginehub.worldeditcui.render.ConfiguredColor;
import org.enginehub.worldeditcui.render.points.PointCube;
import org.enginehub.worldeditcui.render.shapes.Render3DPolygon;
import org.enginehub.worldeditcui.util.Vector3;

public class PolyhedronRegion extends Region {

	private static final Vector3 HALF = new Vector3(0.5, 0.5, 0.5);
	
	private final List<PointCube> vertices = new ArrayList<>();
	private final List<Vector3[]> faces = new ArrayList<>();
	
	private final List<Render3DPolygon> faceRenders = new ArrayList<>();
	
	public PolyhedronRegion(final WorldEdit controller) {
		super(controller, ConfiguredColor.POLYGON_BOX.style(), ConfiguredColor.POLYGON_POINT.style(), ConfiguredColor.CUBOID_POINT_ONE.style());
	}
	
	@Override
	public void render(final RenderContext context) {
		for (final PointCube vertex : this.vertices) {
			vertex.render(context);
		}
		
		for (final Render3DPolygon face : this.faceRenders) {
			face.render(context);
		}
	}

	@Override
	public void setCuboidPoint(final int id, final double x, final double y, final double z) {
		final PointCube vertex = new PointCube(x, y, z).setId(id);
		vertex.setStyle(id == 0 ? this.styles[2] : this.styles[1]);
		
		if (id < this.vertices.size()) {
			this.vertices.set(id, vertex);
		}

		else {
			for (int i = 0; i < id - this.vertices.size(); i++) {
				this.vertices.add(null);
			}

			this.vertices.add(vertex);
		}
	}
	
	@Override
	public void addPolygon(final int[] vertexIds) {
		final Vector3[] face = new Vector3[vertexIds.length];

    for (int i = 0; i < vertexIds.length; ++i) {
			final PointCube vertex = this.vertices.get(vertexIds[i]);

      if (vertex == null) {
				// This should never happen
				return;
			}
			
			face[i] = vertex.getPoint().add(HALF);
		}

		this.faces.add(face);
		this.update();
	}
	
	private void update() {
		this.faceRenders.clear();
		
		for (final Vector3[] face : this.faces) {
			this.faceRenders.add(new Render3DPolygon(this.styles[0], face));
		}
	}
	
	@Override
	protected void updateStyles() {
		for (final PointCube vertex : this.vertices) {
			vertex.setStyle(vertex.getId() == 0 ? this.styles[2] : this.styles[1]);
		}
		
		for (final Render3DPolygon face : this.faceRenders) {
			face.setStyle(this.styles[0]);
		}
	}

	@Override
	public RegionType getType() {
		return RegionType.POLYHEDRON;
	}

}
