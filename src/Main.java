import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.*;


class Main {
    static List<Empleado>empleados;
    public static void main(String[] args) throws IOException {
        cargarArchivo();
        mostrarEmpleadosGananMas();
        mostrarEmpleadosPorDept();
        cantEmpleadosPorDept();
        SumSalariosTotal();
        EmpleadoGanaMasPorDept();
        EmpleadoGanaMas();
        EmpleadoGanaMenos();
        promSalaryDepa();
        promSalaryTotal();
        SumSalarios();

    }
    static void cargarArchivo() throws IOException{
        Pattern pattern =Pattern.compile(";");
        String filename= "empleado.csv";

        try(Stream<String> lines = Files.lines(Path.of(filename))){
            empleados=lines.skip(1).map(line->{
                String[]arr=pattern.split(line);
                return new Empleado(arr[0],arr[1],Double.parseDouble(arr[3]),arr[4]);
            }).collect(Collectors.toList());
            empleados.forEach(System.out::println);
        }
    }
    static Predicate<Empleado> cuatroASeisMil =
            e -> (e.getSalario() >= 4000 && e.getSalario() <= 7000);

    static void mostrarEmpleadosGananMas(){
        System.out.printf(
                "%nEmpleados que ganan $4000-$7000 mensuales ordenados por salario:%n");
        empleados.stream()
                .filter(cuatroASeisMil)
                .sorted(Comparator.comparing(Empleado::getSalario))
                .forEach(System.out::println);
    }
    static void mostrarEmpleadosPorDept(){
        System.out.printf("%nEmpleados por departamento:%n");
        Map<String, List<Empleado>> agrupadoPorDepartamento =
                empleados.stream()
                        .collect(Collectors.groupingBy(Empleado::getDepartamento));
        agrupadoPorDepartamento.forEach(
                (departamento, empleadosEnDepartamento) ->
                {
                    System.out.println(departamento);
                    empleadosEnDepartamento.forEach(
                            empleado -> System.out.printf(" %s%n", empleado));
                }
        );
    }
    static void cantEmpleadosPorDept(){
        // cuenta el número de empleados en cada departamento
        System.out.printf("%nConteo de empleados por departamento:%n");
        Map<String, Long> conteoEmpleadosPorDepartamento =
                empleados.stream()
                        .collect(Collectors.groupingBy(Empleado::getDepartamento,
                                TreeMap::new, Collectors.counting()));
        conteoEmpleadosPorDepartamento.forEach(
                (departamento, conteo) -> System.out.printf(
                        "%s tiene %d empleado(s)%n", departamento, conteo));
    }
    static void SumSalariosTotal(){
        Map<String, List<Empleado>> agrupadoPorDepartamento =
                empleados.stream()
                        .collect(Collectors.groupingBy(Empleado::getDepartamento));
        System.out.printf(
                "%nNomina de cada departamento  %n");
        agrupadoPorDepartamento.forEach(
                (departamento, empleadosEnDepartamento) ->
                {
                    System.out.print(departamento+": ");
                    System.out.println(empleadosEnDepartamento.
                            stream().
                            mapToDouble(Empleado::getSalario)
                            .sum());
                });
    }
    static void EmpleadoGanaMasPorDept(){
        Function<Empleado, Double> porSalario = Empleado::getSalario;
        Comparator<Empleado> SalarioDescendete =
                Comparator.comparing(porSalario);
        System.out.printf("%nEmpleados por departamento: %n");
        Map<String, List<Empleado>> agrupadoPorDepartamento =
                empleados.stream()
                        .collect(Collectors.groupingBy(Empleado::getDepartamento));
        agrupadoPorDepartamento.forEach(
                (departamento, empleadosEnDepartamento) ->
                {
                    System.out.print(departamento+": ");
                    Empleado empleadoMas=empleadosEnDepartamento.stream().sorted(SalarioDescendete.reversed())
                            .findFirst()
                            .get();
                    System.out.println(empleadoMas.getPrimerNombre()+" "+empleadoMas.getApellidoPaterno()+
                            " ///Cuanto money? ==> money: "+empleadoMas.getSalario()+" $$$");
                }
        );
    }
    static void EmpleadoGanaMas(){
        Function<Empleado, Double> porSalario = Empleado::getSalario;
        Comparator<Empleado> SalarioDescendete =
                Comparator.comparing(porSalario);
        Empleado empleadoMas=empleados.stream()
                .sorted(SalarioDescendete.reversed())
                .findFirst()
                .get();
        System.out.printf(
                "%nEmpleado que gana mas: %s %s %s %s%s%n",
                empleadoMas
                .getPrimerNombre(),
                empleadoMas
                        .getApellidoPaterno()," ///Cuanto money? ==> money: ",
                empleadoMas
                        .getSalario()," $$$");
    }
    static void EmpleadoGanaMenos(){
        Function<Empleado, Double> porSalario = Empleado::getSalario;
        Comparator<Empleado> SalarioDescendete =
                Comparator.comparing(porSalario);
        Empleado empleadoMenos=empleados.stream()
                .sorted(SalarioDescendete)
                .findFirst()
                .get();
        System.out.printf(
                "%nEmpleado que gana menos: %s %s %s %s%s%n",
                empleadoMenos.getPrimerNombre(),empleadoMenos.getApellidoPaterno()," ///Cuanto money? ==> money: ",
                empleadoMenos.getSalario(),"$$$");
    }
    static void promSalaryDepa(){
        Map<String, List<Empleado>> agrupadoPorDepartamento =
                empleados.stream()
                        .collect(Collectors.groupingBy(Empleado::getDepartamento));
        System.out.println("\nPromedio de salarios de los empleados por Depa:");
                agrupadoPorDepartamento.forEach((departamento, empleadosporDepa)-> {
                    System.out.print(departamento+": ");
                    System.out.println(empleadosporDepa
                            .stream()
                            .mapToDouble(Empleado::getSalario)
                            .average()
                            .getAsDouble());
                });


    }
    static void promSalaryTotal(){
        System.out.printf("\nPromedio de salarios de todos los depas: %.2f%n",
                empleados.stream()
                        .mapToDouble(Empleado::getSalario)
                        .average()
                        .getAsDouble());

    }
    static void SumSalarios(){
        System.out.printf(
                "%nSuma de todos los salarios: %.2f%n",
                empleados.stream()
                        .mapToDouble(Empleado::getSalario)
                        .sum());
    }


}
