package com.cristian.gastos.ingreso;

import com.cristian.gastos.common.exception.RecursoNoEncontradoException;
import com.cristian.gastos.ingreso.dto.IngresoRequestDTO;
import com.cristian.gastos.ingreso.dto.IngresoResponseDTO;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IngresoService {

    private final IngresoRepository ingresoRepository;

    public IngresoService(IngresoRepository ingresoRepository) {
        this.ingresoRepository = ingresoRepository;
    }

    public IngresoResponseDTO crear(IngresoRequestDTO dto) {
        Ingreso ingreso = new Ingreso(dto.getDescripcion(), dto.getMonto(), dto.getFecha());
        return IngresoResponseDTO.from(ingresoRepository.save(ingreso));
    }

    @Transactional(readOnly = true)
    public List<IngresoResponseDTO> listar(String mes) {
        List<Ingreso> ingresos;
        if (mes != null && !mes.isBlank()) {
            ingresos = ingresoRepository.findByMesOrderByFechaDesc(mes);
        } else {
            ingresos = ingresoRepository.findAll(
                    Sort.by(Sort.Direction.DESC, "fecha"));
        }
        return ingresos.stream().map(IngresoResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public IngresoResponseDTO obtener(Long id) {
        Ingreso ingreso = ingresoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ingreso", id));
        return IngresoResponseDTO.from(ingreso);
    }

    public IngresoResponseDTO actualizar(Long id, IngresoRequestDTO dto) {
        Ingreso ingreso = ingresoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ingreso", id));
        ingreso.setDescripcion(dto.getDescripcion());
        ingreso.setMonto(dto.getMonto());
        ingreso.setFecha(dto.getFecha());
        return IngresoResponseDTO.from(ingresoRepository.save(ingreso));
    }

    public void eliminar(Long id) {
        if (!ingresoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Ingreso", id);
        }
        ingresoRepository.deleteById(id);
    }
}
