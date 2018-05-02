// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../services/report.service';
import { VehicleCount } from '../../classes/vehicle-count';
import { EquipmentDeployed } from '../../classes/equipment-deployed';
import { ParticipantsTrained } from '../../classes/participants-trained';

@Component({
	selector: 'pt-dashboard',
	templateUrl: './dashboard.component.html',
	providers: [ReportService]
})
export class DashboardComponent implements OnInit{
  
    vehicleCounts: VehicleCount[] = []; 
    equipmentDeployed: EquipmentDeployed[] = []; 
    participantsTrained: ParticipantsTrained[] = []; 
	errorMessage: string = '';
  	isLoading: boolean = true;	
  	options: Object;

  	constructor(private reportService : ReportService){  }

	ngOnInit(): void { 

		this.reportService.getEquipmentDeployed().subscribe(
			eq => this.equipmentDeployed = eq,
			e => this.errorMessage = e,
			() => { this.isLoading = false; this.populateEquipmentDeployment(); }
		);

		this.reportService.getVehicleOrganizations().subscribe(
			v => this.vehicleCounts = v,
			e => this.errorMessage = e,
			() => { this.isLoading = false; this.buildChart(); }
		);

        this.reportService.getParticipantsTrained().subscribe(
            p => this.participantsTrained = p,
            e => this.errorMessage = e,
            () => { this.isLoading = false; this.populateParticipantTraining(); }
        );
	}

    populateParticipantTraining(): void {
        for(let t of this.participantsTrained){      
            if(t.type == "Average Training Time")     
                t.label = this.convertMinutesToHoursMinutes(t.count);
        }
    }

    populateEquipmentDeployment(): void {
        for(let e of this.equipmentDeployed){           
            e.label = e.type + "'s Deployed";       
        }
    }

    convertMinutesToHoursMinutes(minutes): string {
        if(minutes == null){
            return null;
        }
        let hours: number = Math.floor(minutes / 60);
        let mins = minutes - (hours * 60);
        if(hours > 0){
            return `${hours} hours ${mins} minutes`;
        }
        else{
            return `${mins} minutes`;
        }
    }

	buildChart(): void {
    	
	    let categoriesList: string[] = [];
        let orgList = new Set();
        let total: number = 0;
        let categoryTotal: number = 0;
		var myMap = new Map();
        var data: Object[] = [];
		var s, c;

		for(s of this.vehicleCounts){
			
            total += s.vehicleCount;
		    if(!myMap.has(s.vehicleClass)){
			    myMap.set(s.vehicleClass, s.vehicleCount);
                categoriesList.push(s.vehicleClass);
			}
			else{
				myMap.set(s.vehicleClass, myMap.get(s.vehicleClass) + s.vehicleCount);
			}
            if(!orgList.has(s.organizationName)){
                orgList.add(s.vehicleClass);
            }
		}
    
        var vcounts = this.vehicleCounts;
        var colors = ['#92373C', '#45557D', '#1A294E', '#7C8F35', '#175D17', '#2C772C', '#5F701B', '#4E070B'];

        let colorCount: number = 0;
        myMap.forEach(function(value, key) {
            categoryTotal = 0;
            var categories: string[] = [];
            var dataVar: number[] = [];
            for(s of vcounts){
                if(s.vehicleClass == key){
                    categoryTotal += s.vehicleCount;
                    categories.push(s.organizationName);
                    dataVar.push(s.vehicleCount);
                }
            }

            var yVar = categoryTotal/total * 100;
         
            var d = {
                y: categoryTotal,
                color: colors[colorCount],
                drilldown: {
                    name: key,
                    categories: categories,
                    data: dataVar,
                    color: colors[colorCount]
                }
            }
            data.push(d);
            colorCount += 1;
        });

        var browserData = [],
        versionsData = [],
        i,
        j,
        dataLen = data.length,
        drillDataLen,
        brightness;

        // Build the data arrays
        for (i = 0; i < dataLen; i += 1) {

            // add browser data
            browserData.push({
                name: categoriesList[i],
                y: data[i]["y"],
                color: data[i]["color"]
            });

            drillDataLen = data[i]["drilldown"].data.length;
            for (j = 0; j < drillDataLen; j += 1) {
                brightness = 20 + (j * 20);
                versionsData.push({
                    name: data[i]["drilldown"].categories[j],
                    y: data[i]["drilldown"].data[j],
                    color: LightenDarkenColor(data[i]["color"], brightness)
                });
            }
        }

		this.options = {
           chart: { type: 'pie', style: { fontFamily: 'Roboto, Helvetica, Arial, sans-serif' } },
           credits:{ enabled: false },
           title: {
                text: 'Number of Trucking Companies Represented: ' + orgList.size,
                style: {
                    fontSize: '14px',             
                },
                margin: 30
            },                      
            yAxis: {
                title: {
                    text: 'Total percent market share'
                }
            },
            plotOptions: {
                pie: {
                    shadow: false,
                    center: ['50%', '50%']
                }
            },
            tooltip: {
            },
                 series: [{
                name: 'Total vehicles',
                data: browserData,
                size: '60%',
                dataLabels: {
                    formatter: function () {
                        return this.y > 5 ? this.point.name : null;
                    },
                    color: '#ffffff',
                    distance: -30
                }
            }, {
                name: 'Vehicles',
                data: versionsData,
                size: '80%',
                innerSize: '60%',
                dataLabels: {
                    formatter: function () {
                        // display only if larger than 1
                        return '<b>' + this.point.name + ':</b> ' + this.y;
                    }
                }
            }]
        };
}
	

}
function LightenDarkenColor(col, amt) {
  
    var usePound = false;
  
    if (col[0] == "#") {
        col = col.slice(1);
        usePound = true;
    }
 
    var num = parseInt(col,16);
 
    var r = (num >> 16) + amt;
 
    if (r > 255) r = 255;
    else if  (r < 0) r = 0;
 
    var b = ((num >> 8) & 0x00FF) + amt;
 
    if (b > 255) b = 255;
    else if  (b < 0) b = 0;
 
    var g = (num & 0x0000FF) + amt;
 
    if (g > 255) g = 255;
    else if (g < 0) g = 0;
 
    return (usePound?"#":"") + (g | (b << 8) | (r << 16)).toString(16);
  
}