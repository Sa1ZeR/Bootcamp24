package com.nexign.bootcamp24;

import com.nexign.bootcamp24.common.facade.CdrFacade;
import com.nexign.bootcamp24.udr.service.ReportGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppCommandRunner implements CommandLineRunner {

    private final CdrFacade cdrFacade;
    private final ReportGeneratorService reportGenerator;

    /**
     * Данный метод запускается, когда спринг уже полностью поднял контекст
     */
    @Override
    public void run(String... args) throws Exception {
        //запускаем метод в зависимости от переданных настроек
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("reportMod")) {
                try {
                    int mod = Integer.parseInt(args[i+1]);

                    switch (mod) {
                        case 0 -> {
                            cdrFacade.cdrStart();
                            reportGenerator.generateReport();
                        }
                        case 1 -> {
                            if(args.length < 3)
                                throw new IllegalArgumentException("Arguments not valid, for example: java -jar <fileName.jar> reportMod 1 <phoneNumber>");

                            cdrFacade.cdrStart();
                            reportGenerator.generateReport(args[i + 2]);
                        }
                        case 2 -> {
                            if(args.length < 4)
                                throw new IllegalArgumentException("Arguments not valid, for example: java -jar <fileName.jar> reportMod 2 <phoneNumber> <month>");

                            cdrFacade.cdrStart();
                            reportGenerator.generateReport(args[i + 2], Integer.parseInt(args[i + 3]));
                        }
                    }
                } catch (NumberFormatException e) {
                    log.error("Invalid report mod: {}", (Object) args);
                    log.error("Use: java -jar <fileName.jar> reportMod <mode> <args>");
                }
                return;
            }
        }

        //если режим не установлен, то запускаем дефолтный метод
        cdrFacade.cdrStart();
        reportGenerator.generateReport();
    }
}
