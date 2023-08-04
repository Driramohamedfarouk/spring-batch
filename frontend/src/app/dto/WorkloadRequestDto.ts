
export interface Range {
    from : number ;
    to : number ;
    step : number ;
}

export interface WorkloadRequestDto {
    jobName : string ;
    nbLinesToReadRange : Range ;
    nbStepsRange : Range ;
    chunkSizeRange : Range ;
}