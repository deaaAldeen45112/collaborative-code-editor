import { Component } from '@angular/core';
// import { Plan } from '../../../core/models/plan-model';
// import { PlanService } from '../../../core/services/plan.service';
// import { ApiResponse } from '../../../core/utils/ApiResponse';
// import { DataSharedService } from '../../../core/services/data-shared.service';
// import { PlanDto } from '../../../core/DTO/get-exam-provider-by-user-id-view-model';

@Component({
  selector: 'app-choose-plan',
  templateUrl: './choose-plan.component.html',
  styleUrl: './choose-plan.component.css'
})
export class ChoosePlanComponent {
  // Plans: Plan[] = [];
  // mostExpensivePlan?: Plan;
  // currentExamProviderPlan!: PlanDto;


  // constructor(
  //   private planService: PlanService,
  //   private planSharedService: DataSharedService

  // ) {}

  // ngOnInit(): void {
  //   this.planSharedService.plan$.subscribe(plan => {
  //     this.currentExamProviderPlan = plan!;
  //     console.log('Current Exam Provider Plan:');
  //   });

  //   this.fetchAllPlansWithFeatures();
  // }

  // fetchAllPlansWithFeatures() {
  //   this.planService.GetAllPlanWithFeatures().subscribe(
  //     (response: ApiResponse<Plan[]>) => {
  //       if (response.status === 200) {
  //         console.log(response);
  //         this.Plans = response.data;

  //         this.mostExpensivePlan = this.Plans.reduce((prev, current) => {
  //           return prev.planPrice > current.planPrice ? prev : current;
  //         }, this.Plans[0]);

  //         console.log('Most expensive plan:', this.mostExpensivePlan);
  //       }
  //     },
  //     (error) => {
  //       console.error('Fetch plans error:', error);
  //     }
  //   );
  // }

  // isMostExpensivePlan(plan: Plan): boolean {
  //   return this.mostExpensivePlan?.planPrice === plan.planPrice;
  // }

}

