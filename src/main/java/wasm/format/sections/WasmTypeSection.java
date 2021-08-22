package wasm.format.sections;

import static ghidra.app.util.bin.StructConverter.BYTE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ghidra.app.util.bin.BinaryReader;
import ghidra.app.util.bin.format.dwarf4.LEB128;
import ghidra.program.model.data.ArrayDataType;
import ghidra.program.model.data.Structure;
import ghidra.util.exception.DuplicateNameException;
import wasm.format.sections.structures.WasmFuncType;

public class WasmTypeSection implements WasmPayload {

	private LEB128 count;
	private List<WasmFuncType> types = new ArrayList<WasmFuncType>();
	
	public WasmTypeSection (BinaryReader reader) throws IOException {
		count = LEB128.readUnsignedValue(reader);
		for (int i =0; i < count.asUInt32(); ++i) {
			types.add(new WasmFuncType(reader));
		}
		
	}
	
	public WasmFuncType getType(int typeidx) {
		return types.get(typeidx);
	}
	
	public int getNumTypes() {
		return types.size();
	}

	@Override
	public void addToStructure(Structure structure) throws IllegalArgumentException, DuplicateNameException, IOException {
		structure.add(new ArrayDataType(BYTE, count.getLength(), BYTE.getLength()), "count", null);
		for (int i = 0; i < count.asUInt32(); ++i) {
			structure.add(types.get(i).toDataType(), types.get(i).toDataType().getLength(), "type_"+i, null);
		}
	}

	@Override
	public String getName() {
		return ".type";
	}
}
